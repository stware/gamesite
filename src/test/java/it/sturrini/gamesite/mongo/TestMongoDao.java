package it.sturrini.gamesite.mongo;

import org.junit.Assert;
import org.junit.Test;

import it.sturrini.gamesite.dao.MongoDao;
import it.sturrini.gamesite.model.Player;

public class TestMongoDao {

	@Test
	public void test() throws InstantiationException, IllegalAccessException {
		Player p = new Player();
		p.setName("name");
		p.setNickname("nickname");
		boolean saved = MongoDao.getInstance(Player.class).saveOrUpdate(p);
		Assert.assertTrue(saved);
		Assert.assertTrue(p.get_id() != null);
		Player retrieved = (Player) MongoDao.getInstance(Player.class).findById(p.get_id().toString());
		Assert.assertTrue(retrieved != null && retrieved.getName().equals("name"));

		saved = MongoDao.getInstance(Player.class).saveOrUpdate(retrieved);
		Assert.assertTrue(saved);
		boolean delete = MongoDao.getInstance(Player.class).delete(retrieved);
		Assert.assertTrue(delete);
	}

}
