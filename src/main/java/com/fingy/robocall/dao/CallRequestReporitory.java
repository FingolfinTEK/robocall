package com.fingy.robocall.dao;

import com.fingy.robocall.model.CallRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallRequestReporitory extends JpaRepository<CallRequest, Long> {

    CallRequest findBySid(String sid);
}
