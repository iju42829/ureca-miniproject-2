package com.ureca.miniproject.game.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.entity.ParticipantStatus;
import com.ureca.miniproject.user.entity.User;

public interface GameParticipantRepository extends JpaRepository<GameParticipant, Long> {

    Boolean existsByUserAndStatus(User user, ParticipantStatus status);
    List<GameParticipant> findAllByGameRoom_Id(Long roomId);    
    Optional<GameParticipant> findByUserAndStatus(User user, ParticipantStatus status);
    void deleteByUserAndGameRoom(User user, GameRoom gameRoom);
    void deleteByUserAndGameRoomAndStatus(User user, GameRoom gameRoom,ParticipantStatus status);
    Optional<GameParticipant> findByUserAndGameRoom_Id(User user, Long roomId);
    @Query("""
    	    SELECT gp.user.userName 
    	    FROM GameParticipant gp
    	    WHERE gp.gameRoom.id = :roomId
    	      AND gp.role = 'MAFIA'
    	      AND gp.status = 'JOINED'
    	""")
    List<String> findMafiaUsernamesByRoomId(@Param("roomId") Long roomId);
    @Query("""
    	    SELECT gp.user.userName 
    	    FROM GameParticipant gp
    	    WHERE gp.gameRoom.id = :roomId
    	      AND gp.role = 'POLICE'
    	      AND gp.status = 'JOINED'
    	""")
    List<String> findPoliceUsernamesByRoomId(@Param("roomId") Long roomId);
    @Query(value = """
    	    SELECT u.user_name
    	    FROM game_participant gp
    	    JOIN users u ON gp.user_id = u.id
    	    WHERE gp.game_room_id = :roomId
    	      AND gp.role = 'POLICE'
    	      AND gp.status = 'JOINED'
    	      AND gp.is_alive = 1
    	""", nativeQuery = true)
    List<String> findAlivePoliceUsernamesByRoomId(@Param("roomId") Long roomId);
    
    
    @Query("""
    		SELECT gp.gameRoom.id
    		 FROM GameParticipant gp
    		 WHERE gp.status = 'INVITED' 
    		 AND  gp.user.Id = :userId
    		""")
    List<Long> findForInvites(@Param("userId") Long userId);

}
