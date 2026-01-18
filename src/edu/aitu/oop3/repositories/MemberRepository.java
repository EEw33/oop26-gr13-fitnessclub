package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member create(Member member);
    List<Member> findAll();
    Optional<Member> findById(long id);
}
