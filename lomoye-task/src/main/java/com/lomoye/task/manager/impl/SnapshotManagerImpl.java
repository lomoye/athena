package com.lomoye.task.manager.impl;

import com.lomoye.common.dao.BasicMapper;
import com.lomoye.task.dao.SnapshotMapper;
import com.lomoye.task.domain.Snapshot;
import com.lomoye.task.manager.SnapshotManager;
import com.lomoye.common.manager.AbstractManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SnapshotManagerImpl extends AbstractManager<Snapshot> implements SnapshotManager {

    @Autowired
    private SnapshotMapper mapper;


    @Override
    protected BasicMapper<Long, Snapshot> getMapper() {
        return mapper;
    }
}
