package repositories;

import java.util.List;

import entities.Address;
import interfaces.IAddressRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class AddressRepository implements IAddressRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Address create(Address address) {
        entityManager.persist(address);
        entityManager.flush();
        entityManager.refresh(address);
        return address;
    }

    @Override
    public List<Address> readAllByUser(int oauthId) {
        TypedQuery<Address> query = entityManager.createQuery(
            "SELECT a FROM Address a WHERE a.oauthId = :oauthId", Address.class
        );
        query.setParameter("oauthId", oauthId);
        List<Address> addresses = query.getResultList();
    
        if (addresses.isEmpty()) {
            throw new EntityNotFoundException("No addresses found for user with id: " + oauthId);
        }
    
        return addresses;
    }

    @Override
    public Address update(Address address) {
        Address existingAddress = entityManager.find(Address.class, address.getId());
        if (existingAddress == null) {
            throw new EntityNotFoundException("Address not found with id: " + address.getId());
        }

        if (!existingAddress.getClass().equals(address.getClass())) {
            throw new IllegalArgumentException(
                    "Address type mismatch: cannot update " + existingAddress.getClass().getSimpleName() +
                            " with " + address.getClass().getSimpleName());
        }

        return entityManager.merge(address);
    }

    @Override
    public void delete(int id) {
        Address address = entityManager.find(Address.class, id);
        if (address == null) {
            throw new EntityNotFoundException("Address not found with id: " + id);
        }
        entityManager.remove(address);
    }
}
