package org.encode.libraryprojectapi.repository;

import org.encode.libraryprojectapi.model.Member;
import org.encode.libraryprojectapi.model.MemberStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MemberRepository extends MongoRepository<Member,String>{
    void deleteById(String id);
    List<Member> findMembersByStatus(MemberStatus status);

}
