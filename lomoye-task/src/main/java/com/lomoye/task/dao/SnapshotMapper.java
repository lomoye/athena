package com.lomoye.task.dao;

import com.lomoye.common.dao.PagedMapper;
import com.lomoye.task.domain.Snapshot;
import org.springframework.stereotype.Repository;

@Repository
public interface SnapshotMapper extends PagedMapper<Long,Snapshot> {
}
