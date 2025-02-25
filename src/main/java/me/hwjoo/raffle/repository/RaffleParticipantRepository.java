package me.hwjoo.raffle.repository;

import me.hwjoo.raffle.domain.Raffle;
import me.hwjoo.raffle.domain.RaffleParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RaffleParticipantRepository extends JpaRepository<RaffleParticipant, Long> {
    
    boolean existsByRaffleIdAndMemberId(Long raffleId, UUID memberId);
    
    Optional<RaffleParticipant> findByRaffleIdAndMemberId(Long raffleId, UUID memberId);
    
    List<RaffleParticipant> findByRaffleAndIsWinnerTrue(Raffle raffle);
    
    @Query("SELECT COUNT(p) FROM RaffleParticipant p WHERE p.raffle.id = :raffleId")
    int countParticipantsByRaffleId(@Param("raffleId") Long raffleId);
    
    @Query(value = "SELECT * FROM raffle_participants WHERE raffle_id = :raffleId ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<RaffleParticipant> findRandomParticipants(@Param("raffleId") Long raffleId, @Param("limit") int limit);
} 