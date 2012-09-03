package be.kuleuven.researchpad.dao;

import org.springframework.stereotype.Repository;

import be.kuleuven.researchpad.domain.RatingItem;

@Repository
public class RatingItemDaoImpl extends GenericJpaDaoImpl<RatingItem, Integer> implements
RatingItemDao {

}
