package com.dinenowinc.dinenow.dao;

import com.dinenowinc.dinenow.model.AddressBook;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDao extends BaseEntityDAOImpl<AddressBook, String> {

  @Inject
  public AddressBookDao(Provider<EntityManager> emf) {
    super(emf);
    entityClass = AddressBook.class;
  }

  public List<AddressBook> getAddressBooksByCustomerId(String customerId) {
    try {
      List<AddressBook> addressBooks = (ArrayList<AddressBook>) getEntityManager().createNativeQuery(
          "SELECT t.* FROM address_book t WHERE t.id_customer = :value", AddressBook.class)
          .setParameter("value", customerId).getResultList();
      return addressBooks;
    }
    catch (Exception e) {
      return null;
    }
  }
}
