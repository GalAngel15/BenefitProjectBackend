package Application.DataAccess;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MiniAppCommadDao extends JpaRepository<EntityCommand, String> {

}