package interfaces;

import java.util.List;

import entities.Address;

public interface IAddressService {
    Address create(Address address);
    List<Address> readAllByUser(int userId);
    Address update(Address address);
    void delete(int id);
}
