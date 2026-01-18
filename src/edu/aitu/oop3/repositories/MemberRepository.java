package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.Member;
import java.util.List;

public interface MemberRepository {
    Member create(Member member);
    List<Member> findAll();
    Member findById(long id);
}
