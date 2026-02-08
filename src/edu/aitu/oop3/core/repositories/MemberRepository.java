package edu.aitu.oop3.core.repositories;

import edu.aitu.oop3.core.entities.Member;

public interface MemberRepository extends Repository<Member, Long> {
    void update(Member member);

    void save(Member member);
}
