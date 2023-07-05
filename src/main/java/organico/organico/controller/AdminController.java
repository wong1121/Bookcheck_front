package organico.organico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import organico.organico.domain.Item;
import organico.organico.service.AdminService;
import organico.organico.service.ItemService;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {
	private final ItemService itemService;
	private final AdminService adminService;
	
	@Autowired
	public AdminController(ItemService itemService, AdminService adminService) {
		this.itemService = itemService;
		this.adminService = adminService;
	}
	
	// [관리자 페이지]

		// 상품 관리(목록)
		@GetMapping("/items")
		public String adminItemList(Model model) {
			List<Item> items = itemService.findAll();
			model.addAttribute("items", items);
			return "admin/items";
		}
		
		// 상품 등록 폼
		@GetMapping("/add")
		public String addForm() {
			return "admin/add";
		}
		
		// 상품 등록
		@PostMapping("/add")
		public String addItem(@ModelAttribute Item item, 
				@RequestParam MultipartFile image, 
				RedirectAttributes attribute) throws Exception {
			
			adminService.saveItem(item, image);
			attribute.addAttribute("id", item.getId());
			return "redirect:/admin/items/{id}";
		}
		
		// 상품 상세
		@GetMapping("/items/{id}")
		public String item(@PathVariable Long id, Model model) {
			Item item = itemService.findId(id).orElseGet(Item::new);
			model.addAttribute("item", item);
			return "admin/item";
		}
		
		// 상품 수정 폼
		@GetMapping("/items/{id}/edit")
		public String editForm(@PathVariable Long id, Model model) {
			Item item = itemService.findId(id).get();
			model.addAttribute("item", item);
			return "admin/edit";
		}
		
		// 상품 수정
		@PostMapping("/items/{id}/edit")
		public String edit(@PathVariable Long id, @ModelAttribute Item item, RedirectAttributes redirectAttributes) throws Exception {
			adminService.updateItem(id, item);
			redirectAttributes.addAttribute("id", id);
			return "redirect:/admin/items/{id}";
		}
		
		// 상품 삭제
		@GetMapping("/items/{id}/delete")
		public String deleteItem(@PathVariable Long id) {
			adminService.deleteItem(id);
			return "redirect:/admin/items";
		}
}
