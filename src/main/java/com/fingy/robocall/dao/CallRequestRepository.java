package com.fingy.robocall.dao;

import com.fingy.robocall.model.CallRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CallRequestRepository extends JpaRepository<CallRequest, Long> {
    CallRequest findBySid(String sid);
}
