package ovh.kg4.devicetracker.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ovh.kg4.devicetracker.entity.User;

@Repository
public interface UserRepo extends PagingAndSortingRepository<User, Long>, ListCrudRepository<User, Long> {

}
