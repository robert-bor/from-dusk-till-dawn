package nl.d2n.service;

import nl.d2n.dao.UniqueDistinctionDao;
import nl.d2n.model.UniqueDistinction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardService {

    @Autowired
    private UniqueDistinctionDao uniqueDistinctionDao;

    public List<UniqueDistinction> findUniqueDistinctions() {
        return this.uniqueDistinctionDao.findUniqueDistinctions();
    }

}
