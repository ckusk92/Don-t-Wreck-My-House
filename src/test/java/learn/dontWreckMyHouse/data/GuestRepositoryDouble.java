package learn.dontWreckMyHouse.data;

import learn.dontWreckMyHouse.models.Guest;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class GuestRepositoryDouble implements GuestRepository{

    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";
    private final String filePath;

    public GuestRepositoryDouble(@Value("./data/guests-test.csv") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Guest> findAll() {
        return null;
    }

    @Override
    public Guest findById(int id) {
        return null;
    }

    @Override
    public Guest add(Guest guest) throws DataException {
        return null;
    }
}
