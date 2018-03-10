package com.genpact.bi.batch.step;

import org.springframework.batch.item.ItemProcessor;

//import com.genpact.bi.batch.dto.CustomPojo;
import com.genpact.bi.batch.entity.Pojo;
import com.genpact.bi.batch.entity.Pojo1;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomItemProcessor implements
		ItemProcessor<Pojo, Pojo1> {

	@Override
	public Pojo1 process(final Pojo pojo) throws Exception {
		log.info("Entering CustomProcessor process method ...");
        Pojo1 pojo1 = new Pojo1();
        pojo1.setId(pojo.getId());
        pojo1.setDescription(pojo.getDescription());
		//final Pojo encodedPojo = new Pojo(id, desc);

		return pojo1;

	}

	private String encode(String word) {
		StringBuffer str = new StringBuffer(word);
		return str.reverse().toString();
	}

}