package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.Member;

public interface MemberRepository extends Repository<Member, Long> {
    void update(Member member);
}
