package me.hwjoo.raffle.repository;

import me.hwjoo.raffle.domain.Raffle;
import me.hwjoo.raffle.domain.RaffleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RaffleRepository extends JpaRepository<Raffle, Long> {
    
    Page<Raffle> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    List<Raffle> findByStatusOrderByStartDateAsc(RaffleStatus status);
    
    @Query("SELECT r FROM Raffle r WHERE r.status = 'SCHEDULED' AND r.startDate <= :now")
    List<Raffle> findScheduledRafflesToActivate(@Param("now") LocalDateTime now);
    
    @Query("SELECT r FROM Raffle r WHERE r.status = 'ACTIVE' AND r.endDate <= :now")
    List<Raffle> findActiveRafflesToClose(@Param("now") LocalDateTime now);
    
    @Query("SELECT r FROM Raffle r WHERE r.status = 'CLOSED' AND r.drawDate <= :now")
    List<Raffle> findClosedRafflesToDraw(@Param("now") LocalDateTime now);
} 