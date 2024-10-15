package Repository;


import Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository extends JpaRepository<Category, long> {

}
