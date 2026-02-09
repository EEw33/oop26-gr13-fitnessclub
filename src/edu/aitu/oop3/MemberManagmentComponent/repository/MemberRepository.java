package edu.aitu.oop3.MemberManagmentComponent.repository;

import edu.aitu.oop3.CoreComponent.repositories.Repository;
import edu.aitu.oop3.MemberManagmentComponent.entities.Member;

public interface MemberRepository extends Repository<Member, Long> {
    void update(Member member);

    void save(Member member);
}
