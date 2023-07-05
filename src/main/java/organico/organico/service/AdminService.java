package organico.organico.service;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import organico.organico.domain.Item;
import organico.organico.repository.AdminRepository;
import organico.organico.repository.ItemRepository;

@Service
public class AdminService {
	
	private final AdminRepository adminRepository;
	
	public AdminService(AdminRepository adminRepository) {
		this.adminRepository = adminRepository;
	}
	
	public Item saveItem(Item item, MultipartFile image) throws Exception {
		String oriImgName = image.getOriginalFilename();
		String imgName = "";
		
		String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/img/item/";
		
		// 파일명 중복 방지를 위해 파일명 새로 생성 --> imgName에 저장
		// UUID는 서로 다른 객체를 구별하기 위한 클래스로, 현재 시간을 기준으로 난수화시킴
		UUID uuid = UUID.randomUUID();
		String savedFileName = uuid + "_" + oriImgName;
		
		imgName = savedFileName;
		
		File saveFile = new File(projectPath, imgName);
		
		image.transferTo(saveFile);
		
		item.setImgName(imgName);
		item.setImgPath("/img/item/" + imgName);
		
		adminRepository.save(item);
		
		return item;
	}
	
	public void deleteItem(Long id) {
		adminRepository.deleteItem(id);
	}
	
	public Item updateItem(Long id, Item item) {
		adminRepository.updateItem(id, item);
		return item;
	}
}
