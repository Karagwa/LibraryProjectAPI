package org.encode.libraryprojectapi.repository;

import org.encode.libraryprojectapi.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member,String> {
}
