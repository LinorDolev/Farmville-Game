package farmground.dal.utils;

import org.springframework.stereotype.Service;

import farmground.dal.NumberGenerator;
import farmground.dal.NumberGeneratorDao;

@Service
public class IdGeneratorServiceJPA implements IdGeneratorService {

	private NumberGeneratorDao idGeneratorDao;
	
	public IdGeneratorServiceJPA(NumberGeneratorDao idGeneratorDao) {
		setIdGeneratorDao(idGeneratorDao);
	}
	
	@Override
	public String nextId() {
		NumberGenerator temp = this.idGeneratorDao.save(new NumberGenerator());
		String number = "" + temp.getNextNumber();
		this.idGeneratorDao.delete(temp);
		return number;
	}
	
	public void setIdGeneratorDao(NumberGeneratorDao idGeneratorDao) {
		this.idGeneratorDao = idGeneratorDao;
	}

}
