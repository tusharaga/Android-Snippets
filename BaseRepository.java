package com.fieldez.android.domain.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import android.database.sqlite.SQLiteConstraintException;

import com.fieldez.android.ORMLiteDataHelper;
import com.fieldez.android.domain.model.BaseModel;
import com.fieldez.android.util.DatabaseHelper;
import com.fieldez.android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.table.TableUtils;

public class BaseRepository<K extends BaseModel>  {
	private static final String TAG = BaseRepository.class.getSimpleName();
	public Class<K> type;
	DatabaseHelper dbHelper;
	Dao<K, Integer> dao;
	public BaseRepository(Class<K> type) {
		this.type = type;
		try {
			dbHelper = ORMLiteDataHelper.getInstance().getDatabaseHelper();
			dao = DaoManager.createDao(dbHelper.getConnectionSource(), type);
		} catch (SQLException e) {
			Log.e(TAG, "Eception in Creating DAO", e);
		}
	}

	public K getSingleEntityByProperty(String propertyName, Object value) {
		List<K> list = null;
		try {
			QueryBuilder<K, Integer> qb = getDao(type).queryBuilder();
			SelectArg selectArg = new SelectArg();
			qb.where().eq(propertyName, selectArg);
			PreparedQuery<K> preparedQuery = qb.prepare();
			selectArg.setValue(value);
			list = getDao(type).query(preparedQuery);
			//list = (List<K>) getDao(type).queryForEq(propertyName, value);
			if(list != null && list.size() > 0)			
				return list.get(0);
			else  return null;
		} catch (SQLException e) {
			Log.e(TAG, "Eception in getSingleEntityByProperty ", e);
		}
		return null;
	}


	public List<K> getEntityByProperty(String propertyName, Object value) {
		List<K> list;
		try {
			QueryBuilder<K, Integer> qb = getDao(type).queryBuilder();
			SelectArg selectArg = new SelectArg();
			qb.where().eq(propertyName, selectArg);
			PreparedQuery<K> preparedQuery = qb.prepare();
			selectArg.setValue(value);
			list = getDao(type).query(preparedQuery);
			//list = (List<K>) getDao(type).queryForEq(propertyName, value);
			return list;
		} catch (SQLException e) {
		}
		return null;
	}

	public int persist(K k) {
		try {
			return createDao(type).create(k);
		} catch (SQLException e) {
			Log.e(TAG, "SQLException in persist", e);
		} catch (SQLiteConstraintException e) {
			Log.e(TAG, "SQLiteConstraintException in persist" +  e.getMessage().concat("").concat(k.toString()));
		} catch (Exception e) {
			Log.e(TAG, "Exeption in persist" +  e.getMessage().concat("").concat(k.toString()));
		}
		return 0;
	}

	public List<K> getDirty() {
		try {
			QueryBuilder<K, Integer> qb = getDao(type).queryBuilder();
			qb.orderBy("id", true);
			qb.where().eq("dirty", true);
			return qb.query();
		} catch (SQLException e) {
			Log.e(TAG, "Exception Getting dirty", e);
		}
		return null;

	}

	public int update(K k) {
		try {
			return getDao(type).update(k);
		} catch (SQLException e) {
			Log.e(TAG, "Exception", e);
		}
		return 0;
	}

	public void deleteAll() {
		try {
			TableUtils.clearTable(dbHelper.getConnectionSource(), type);
		} catch (SQLException e) {
			Log.e(TAG, "Exeption" +  e.getMessage());
		}
	}

	public K getEntity(int id) throws SQLException {
		return getDao(type).queryForId(id);
	}

	public int saveOrUpdate(K k) {
		try {
			getDao(type).createOrUpdate(k);
			return k.getId();
		} catch (SQLException e) {
			Log.e(TAG, "SQLException" +  e.getMessage().concat("").concat(k.toString()));
		} catch (SQLiteConstraintException e) {
			Log.e(TAG, "SQLiteConstraintException" +  e.getMessage().concat("").concat(k.toString()));
		} catch (Exception e) {
			Log.e(TAG, "Exeption" +  e.getMessage().concat("").concat(k.toString()));
		}
		return 0;
	}

	public K getById(int id) {
		try {
			return getDao(type).queryForId(id);
		} catch (SQLException e) {
			Log.e(TAG, "Exeption", e);
		}
		return null;
	}

	protected Dao<K, Integer> getDao(Class<K> k) throws SQLException {
		return dbHelper.getDao(k);
	}

	protected Dao<K, Integer> createDao(Class<K> k) {
		return dao;
	}

	public boolean isExists(int id) {
		try {
			return getDao(type).idExists(id);
		} catch (SQLException e) {
			Log.e(TAG, "Exception", e);
		}
		return false;
	}

	public List<K> getByFieldValues(Map<String, Object> filter) {
		try {
			return getDao(type).queryForFieldValues(filter);
		} catch (SQLException e) {
			Log.e(TAG, "Exeption", e);
		}
		return null;
	}


	public boolean isDirty(int id) {
		return getById(id).isDirty();
	}


	public List<K> getAll() {
		try {
			return getDao(type).queryForAll();
		} catch (SQLException e) {
			Log.e(TAG, "Exeption", e);
		}
		return null;
	}

	public K getSingle() {
		try {
			List<K> l = getDao(type).queryForAll();
			if(l != null && l.size() > 0)
				return l.get(0);
		} catch (SQLException e) {
			Log.e(TAG, "Exeption", e);
		}
		return null;
	}


	public int delete(List<K> k) {
		try {
			return getDao(type).delete(k);
		} catch (SQLException e) {
			Log.e(TAG, "Exeption", e);
		}
		return 0;
	}

	public int deleteById(int id) {
		try {
			return getDao(type).deleteById(id);
		} catch (SQLException e) {
			Log.e(TAG, "Exeption", e);
		}
		return 0;
	}
}
