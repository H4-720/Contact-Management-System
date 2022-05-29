package com.smart.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entity.Contact;
import com.smart.entity.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	
	@ModelAttribute
	public void commandhandler(Model model, Principal principal) {
		String username = principal.getName();
		User user =  userRepository.getUserByUserName(username);
		
		model.addAttribute("user", user);
	}
	
	@RequestMapping("/index")
	public String dashboard(Model model) {
		model.addAttribute("title","user dashboard");
		return "normal/udashboard";
	}
	
	//add contact
	@GetMapping("/add-contact")
	public String opencontactadding(Model model) {
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/Add-contact";
	}
	@PostMapping("/process-contact")
	public String processcontact(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file,Model model,
			Principal principal,HttpSession session) {
		try{
		String name=principal.getName();
		User user = this.userRepository.getUserByUserName(name); 
		if(file.isEmpty()) {
			contact.setImage("contact.png");
			
		}else {
			contact.setImage(file.getOriginalFilename());
			File savefile = new ClassPathResource("static/img").getFile();
			Path path=Paths.get(savefile.getAbsolutePath()+ File.separator + file.getOriginalFilename());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("file is uploaded");
		}
		
		contact.setUser(user);
		user.getContacts().add(contact);
		this.userRepository.save(user);
		/* message */
		session.setAttribute("message", new Message("Your contact has been added","success"));
		}catch(Exception e) {
			System.out.println("Error"+ e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wronge","denger"));
		}
		
		return "normal/Add-contact";
	}
	
	/* show contact handler */
	@GetMapping("/show-contact/{page}")
	public String showContact(@PathVariable("page") Integer page,Model model,Principal principal) {
		model.addAttribute("title", "show Contact");
		String name=principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		Pageable pageable= PageRequest.of(page, 5);
		Page<Contact> contact= this.contactRepository.findContactsByUser(user.getId(),pageable); 
		model.addAttribute("contact",contact);
		model.addAttribute("currentpage",page);
		model.addAttribute("totalpage",contact.getTotalPages()); 
		return "normal/show-contact";
	}
	@GetMapping("/{id}/contact")
	public String getcontactdetail(@PathVariable("id") Integer id,Model model,Principal principal) {
		Optional<Contact> contactg =this.contactRepository.findById(id);
		Contact contact=contactg.get();
		String username = principal.getName();
		User user=this.userRepository.getUserByUserName(username);
		
		
		if(user.getId()==contact.getUser().getId()) 
		model.addAttribute("contact",contact);
		
		return"normal/contact_detail";
	}
	@GetMapping("/delete/{id}")
	public String delectcontact(@PathVariable("id") Integer id, Model model,Principal principal) {
		Contact contact = this.contactRepository.findById(id).get(); 
		this.contactRepository.deleteById(contact.getId());
		
		return "redirect:/user/show-contact/0";
	}
	@GetMapping("/update_contact/{cid}")
	public String updatecontact(@PathVariable("cid") Integer id,Model model) {
		
		Contact contact = this.contactRepository.findById(id).get();
		
		model.addAttribute("contact", contact);
		return "normal/updatecontact";
	}
	
	@PostMapping("/Update_contact")
	public String updateprocess(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,
			HttpSession session,Model model,Principal principle) {
		
		try {
			
			Contact oldcontact = this.contactRepository.findById(contact.getId()).get();
			
			if(!file.isEmpty()) {
				
				/* DELETE OLD IMAGE */
				File deletefile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deletefile,oldcontact.getImage());
				file1.delete();
				
				/* Update new image */
				File savefile = new ClassPathResource("static/img").getFile();
				Path path=Paths.get(savefile.getAbsolutePath()+ File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
			}
			else {
				contact.setImage(oldcontact.getImage());
			}
			
			User user=this.userRepository.getUserByUserName(principle.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Contact is updated..","success"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return "redirect:/user/"+contact.getId()+"/contact";
	}
	
	/* profile */
	@GetMapping("/profile")
	public String profileHandle(Model model,Principal principal) {
		String username = principal.getName();
		User user=this.userRepository.getUserByUserName(username);
		model.addAttribute("user",user);
		return "normal/profile";
	}
}


