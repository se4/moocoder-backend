package com.moekr.moocoder.logic.service.impl;

import com.moekr.moocoder.data.dao.CommitDAO;
import com.moekr.moocoder.data.dao.RecordDAO;
import com.moekr.moocoder.data.dao.ResultDAO;
import com.moekr.moocoder.data.entity.Commit;
import com.moekr.moocoder.data.entity.Problem;
import com.moekr.moocoder.data.entity.Record;
import com.moekr.moocoder.data.entity.Result;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@CommonsLog
public class CommitInitializer {
	private final CommitDAO commitDAO;
	private final RecordDAO recordDAO;
	private final ResultDAO resultDAO;

	public CommitInitializer(CommitDAO commitDAO, RecordDAO recordDAO, ResultDAO resultDAO) {
		this.commitDAO = commitDAO;
		this.recordDAO = recordDAO;
		this.resultDAO = resultDAO;
	}

	@Transactional
	public boolean initializeCommit(int resultId, String commitHash) {
		Result result = resultDAO.findById(resultId);
		LocalDateTime now = LocalDateTime.now();
		if (result != null && result.getExam().getEndAt().isAfter(now)) {
			Commit lastBuiltCommit = lastBuiltCommit(resultId);
			Set<Problem> problems;
			if (lastBuiltCommit != null) {
				problems = lastBuiltCommit.getRecords().stream()
						.filter(r -> r.getScore() < 100)
						.map(Record::getProblem)
						.collect(Collectors.toSet());
			} else {
				problems = result.getExam().getProblems();
			}
			if (!problems.isEmpty()) {
				Commit newCommit = new Commit();
				newCommit.setHash(commitHash);
				newCommit.setResult(result);
				newCommit = commitDAO.save(newCommit);
				for (Problem problem : problems) {
					Record record = new Record();
					record.setNumber(-1);
					record.setCommit(newCommit);
					record.setProblem(problem);
					recordDAO.save(record);
				}
				result.setLastCommitAt(now);
				resultDAO.save(result);
				return true;
			}
		}
		return false;
	}

	private Commit lastBuiltCommit(int resultId) {
		List<Commit> commitList = commitDAO.findAllByResult_IdAndFinishedOrderByIdAsc(resultId, true);
		if (!commitList.isEmpty()) {
			return commitList.get(commitList.size() - 1);
		}
		return null;
	}
}
