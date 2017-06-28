package com.lomoye.common.manager;

import com.lomoye.common.dao.BasicMapper;
import com.lomoye.common.dao.VersionedMapper;
import com.lomoye.common.domain.VersionedDomain;
import com.lomoye.common.exception.BusinessException;

import java.util.Date;

/**
 * 抽象类实现version.
 */
public abstract class AbstractVersionedManager<T extends VersionedDomain> extends AbstractManager<T> implements VersionedManager<Long, T> {
    @Override
    public int update(T obj) {
        throw new UnsupportedOperationException(getMapper().getClass() + " is not support update, use updateByPrimaryKeyWithVersion instead");
    }

    @Override
    public int saveOrUpdate(T obj) {
        throw new UnsupportedOperationException(getMapper().getClass() + " is not support saveOrUpdate, use updateByPrimaryKeyWithVersion instead");
    }

    @Override
    public int updateByPrimaryKeyWithVersion(T obj) {
        obj.setModifyTime(new Date());
        BasicMapper<Long, T> mapper = getMapper();
        if (!(mapper instanceof VersionedMapper)) {
            throw new UnsupportedOperationException(getMapper().getClass() + " is not VersionedMapper class");
        }

        int cnt = ((VersionedMapper<Long, T>) mapper).updateByPrimaryKeyWithVersion(obj);
        if (cnt != 1) {
            throw new BusinessException(getMapper().getClass() + " updateByPrimaryKeyWithVersion error|obj_id=" + obj.getId() + "|version=" + obj.getVersion());
        }
        obj.setVersion(obj.getVersion() + 1);  //version更新成功应该+1?
        return cnt;
    }
}
