package com.moekr.aes.logic.service;

import com.moekr.aes.logic.vo.ProblemVO;
import com.moekr.aes.util.enums.ProblemType;
import com.moekr.aes.util.exceptions.ServiceException;
import com.moekr.aes.web.dto.ProblemDTO;
import org.springframework.data.domain.Page;

public interface ProblemService {
	ProblemVO create(int userId, ProblemDTO problemDTO, byte[] content) throws ServiceException;

	Page<ProblemVO> retrievePage(int userId, int page, int limit, ProblemType type) throws ServiceException;

	ProblemVO retrieve(int userId, int problemId) throws ServiceException;

	ProblemVO update(int userId, int problemId, ProblemDTO problemDTO) throws ServiceException;

	void update(int userId, int problemId, String path, byte[] content) throws ServiceException;

	void delete(int userId, int problemId) throws ServiceException;

	ProblemVO create(ProblemDTO problemDTO, byte[] content) throws ServiceException;

	Page<ProblemVO> retrievePage(int page, int limit, ProblemType type) throws ServiceException;

	ProblemVO retrieve(int problemId) throws ServiceException;

	ProblemVO update(int problemId, ProblemDTO problemDTO) throws ServiceException;

	void update(int problemId, String path, byte[] content) throws ServiceException;

	void delete(int problemId) throws ServiceException;
}
