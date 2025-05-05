package br.com.victorbarberino.portfolio.domain.admin;

import br.com.victorbarberino.portfolio.domain.contact.ContactService;
import br.com.victorbarberino.portfolio.domain.contact.ContactData;
import br.com.victorbarberino.portfolio.system.generics.WebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/contacts")
public class AdminContactController extends WebController {
    @Autowired
    private ContactService contactService;

    @GetMapping
    public ModelAndView dispatchContactView() {
        ModelAndView mv = new ModelAndView("admin/contact/contacts");
        mv.addObject("contacts", contactService.listAllContacts());
        return dispatchMv(mv);
    }

    @PostMapping
    public ResponseEntity<Void> saveContact(@ModelAttribute ContactData contactData) {
        contactService.saveContact(contactData);
        return ResponseEntity.ok().build();
    }
}
