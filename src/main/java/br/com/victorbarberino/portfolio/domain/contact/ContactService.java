package br.com.victorbarberino.portfolio.domain.contact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    public ContactData saveContact(ContactData contactData) {
        return contactRepository.saveAndFlush(new ContactEntity(contactData)).convertToData();
    }

    public List<ContactData> listAllContacts() {
        return contactRepository.findAll().stream().map(ContactEntity::convertToData).toList();
    }
}
