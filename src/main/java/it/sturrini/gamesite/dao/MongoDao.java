package it.sturrini.gamesite.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

import it.sturrini.common.DateUtils;
import it.sturrini.gamesite.api.conf.ResourceUtil;
import it.sturrini.gamesite.dao.mongo.MongoClient;
import it.sturrini.gamesite.dao.mongo.MongoUtils;
import it.sturrini.gamesite.model.BaseEntity;
import it.sturrini.gamesite.model.EntitiesEnum;

public class MongoDao<T extends BaseEntity> {

	private static final Logger log = LogManager.getLogger(MongoDao.class);

	private static Map<Class, MongoDao> instanceMap = new HashMap<>();
	private Class dtoClazz;

	private MongoClient client;
	private MongoDatabase db;
	private MongoCollection<Document> c;

	private MongoDao(Class clazz) {
		client = MongoUtils.getClient();
		db = client.getDatabase();
		this.dtoClazz = clazz;
		c = db.getCollection(EntitiesEnum.valueOf(clazz.getSimpleName()).collectionName()).withWriteConcern(WriteConcern.ACKNOWLEDGED);
	}

	public static MongoDao getInstance(Class clazz) {
		if (!instanceMap.containsKey(clazz)) {
			instanceMap.put(clazz, new MongoDao(clazz));
		}

		return instanceMap.get(clazz);
	}

	public T findById(String id) {
		try {
			Document elem = c.find(new Document("_id", new ObjectId(id))).first();
			if (elem != null) {
				return entityToDTO(elem);
			}
		} catch (Exception e) {
			log.error("Unable to load page: " + id + ". " + e.getMessage());
		}

		return null;
	}

	public List<T> findParallel(Document q, Integer offset, Integer limit, Double sample) {

		List<T> res = new ArrayList<>();

		FindIterable<Document> cursor = c.find(q);
		setPagination(cursor, offset, limit);

		MongoCursor<Document> i = cursor.iterator();

		List<Document> list = new ArrayList<>();

		if (sample != null && sample > 0d && sample < 1d && limit > 0) {
			list = getSampledList(i, sample, limit);
		} else {
			while (i.hasNext()) {
				list.add(i.next());
			}
		}

		Map<String, T> map = new ConcurrentHashMap<>();

		list.parallelStream().forEach(d -> {

			T tl = entityToDTO(d);

			if (tl != null && tl.get_id() != null) {
				map.put(tl.get_id().toHexString(), tl);
			}

		});

		for (String key : map.keySet()) {
			res.add(map.get(key));
		}

		return res;
	}

	// private List<Document> getSampledList(MongoCursor<Document> i, SearchFilter sf) {
	//
	// Double sampleRating = sf.getSampleRating();
	// Integer limit = sf.getLimit();
	//
	// return getSampledList(i, sampleRating, limit);
	// }

	private List<Document> getSampledList(MongoCursor<Document> i, Double sampleRating, Integer limit) {

		List<Document> list = new ArrayList<>();

		BigDecimal s = new BigDecimal(sampleRating).setScale(2, BigDecimal.ROUND_HALF_EVEN);

		double sampledLimit = s.multiply(new BigDecimal(limit)).setScale(0, BigDecimal.ROUND_UP).doubleValue();

		double pages = limit / sampledLimit;

		int tempCounter = 0;
		while (i.hasNext()) {
			Document d = i.next();
			if (Math.floor(tempCounter % pages) == 0) {
				list.add(d);
			}
			tempCounter++;
		}

		return list;
	}

	private void setPagination(FindIterable<Document> c, int offset, int limit) {
		c.skip((offset - 1) * 50);

		if (limit > 0) {
			c.limit(limit);
		}
	}

	public T findByIds(String entityName, Long entityId, String relEntityName, Long relEntityId) {
		List<Document> andConditions = new ArrayList<>();
		andConditions.add(new Document("entityName", entityName));
		andConditions.add(new Document("entityId", entityId));
		andConditions.add(new Document("relEntityName", relEntityName));
		andConditions.add(new Document("relEntityId", relEntityId));

		Document db = c.find(new Document("$and", andConditions)).first();
		if (db != null) {
			return entityToDTO(db);
		}

		return null;
	}

	public List<T> findByFilter(SearchFilter sf) {
		List<Document> andConditions = new ArrayList<>();
		if (sf != null) {
			Set<Entry<String, Object>> entrySet = sf.getParams().entrySet();
			for (Entry<String, Object> entry : entrySet) {
				andConditions.add(new Document(entry.getKey(), entry.getValue()));
			}
		}

		FindIterable<Document> db;
		List<T> out = new ArrayList<>();
		if (andConditions.size() > 0) {
			Document filter = new Document("$and", andConditions);
			db = c.find(filter);
		} else {
			db = c.find();
		}
		if (db != null) {
			MongoCursor<Document> it = db.iterator();
			while (it.hasNext()) {
				out.add(entityToDTO(it.next()));
			}
		}

		return out;
	}

	public boolean saveOrUpdate(T p) {
		boolean res = false;
		Long now = DateUtils.nowInMillis();
		if (p.getInsertTimestamp() == null) {
			p.setInsertTimestamp(now);
		}
		p.setLastEditTimestamp(now);
		Document o = dtoToEntity(p);
		o.remove("_id");

		try {
			if (p.get_id() == null) {

				c.insertOne(o);
				p.set_id((ObjectId) o.get("_id"));
			} else {

				c.updateOne(new Document("_id", p.get_id()), new Document("$set", o));
			}

			res = true;
		} catch (Exception e) {
			log.error("Unable to save page", e);
		}

		return res;
	}

	public boolean delete(T p) {
		DeleteResult result = c.deleteOne(Filters.eq("_id", p.get_id()));

		return result.getDeletedCount() == 1;
	}

	public boolean delete(String entityName) {
		boolean res = false;

		try {
			List<Document> andConditions = new ArrayList<>();
			andConditions.add(new Document("entityName", entityName));

			c.deleteMany(new Document(new Document("$and", andConditions)));
			res = true;
		} catch (Exception e) {
			log.error("Unable to delete relation", e);
		}

		return res;
	}

	private Document dtoToEntity(T p) {
		return Document.parse(ResourceUtil.beanToJson(p));
	}

	private T entityToDTO(Document db) {
		ObjectId objectId = db.get("_id", ObjectId.class);
		T bean = (T) ResourceUtil.jsonToBean(db.toJson(), dtoClazz);
		return bean;
	}

}
