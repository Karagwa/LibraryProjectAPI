package org.encode.libraryprojectapi.repository;

import org.encode.libraryprojectapi.model.Member;
import org.encode.libraryprojectapi.model.MemberStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryCustomClass implements MemberRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void deactivateById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("status", MemberStatus.DEACTIVATED);
        mongoTemplate.updateFirst(query, update, Member.class);
    }
}

