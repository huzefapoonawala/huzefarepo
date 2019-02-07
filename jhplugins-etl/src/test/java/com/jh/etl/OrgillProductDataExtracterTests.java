package com.jh.etl;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jh.etl.common.dto.CategoryDto;
import com.jh.etl.common.dto.ProductDto;
import com.jh.etl.common.enums.Supplier;
import com.jh.etl.common.interfaces.DataExtracter;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrgillProductDataExtracterTests extends TestSetupWithMockingFtpReader {
	
	@Autowired private DataExtracter<List<ProductDto>> orgillProductDataExtracter;
	
	@Test
	public void testExtractData() {
		List<ProductDto> list = orgillProductDataExtracter.extractData();
		assertThat(list, IsCollectionWithSize.hasSize(11));
		assertThat(list, IsCollectionContaining.hasItem(
				ProductDto.builder()
					.sku("0010041")
					.category(CategoryDto.builder().code("1010610605").supplier(Supplier.ORGILL).build())
					.title("07661/07561 TOOL HOLDER 24IN")
					.description("Hold and organize tools in garages, workshops, and work stations. Magnetic tool bar that holds 20# per inch. Easily mounts to walls , studs or workbench. Mounting screws included. 4-color packaging for retail.")
					.width(9.75f)
					.height(5.5f)
					.length(26.75f)
					.weight(4.17f)
					.upc("095421076611")
					.retailPrice(16.99d)
					.retailUnit("CD")
					.imageLink("0010041.JPG")
					.supplier(Supplier.ORGILL)
					.build()
				));
	}
	
	@Test
	public void testDescriptionParsing() { 
		List<ProductDto> list = orgillProductDataExtracter.extractData(); 
		assertThat(list, IsCollectionContaining.hasItem(HasPropertyWithValue.hasProperty("description", Is.is("The ChainMax 1000 garage door opener features a quiet traditional chain drive system that has a rugged, time-tested superior design that is dependable, durable, reliable and long-lasting; the unit has a lightweight 3/4 HPc 140 Volt DC motor that allows for quiet, smooth operation with soft starts and soft stops; the durable chain paired with a precision machined motor and gearbox that are greased and factory sealed from the environment, provide quiet and maintenance free operation; accentuating an enhanced design, the low profile c-channel rail with flush-mounting capability, features an enclosed rail to help protect against dirt and debris; equipped with \"GenieSense\" Monitoring and Diagnostic Technology, which minimizes noise wear and tear on the door and improves overall safety; for easier, faster installation the ChainMax includes \"SmartSet\" push button programming; the comprehensive \"Safe-T-Beam\" system meets or exceeds all UL, state, federal and CSA regulations and incorporates an auto reversing system that stops and reverses a closing door if anything passes through the beam; with Auto Seek Dual Frequency, the system will automatically seek either the 315 or 390 MHz frequency produced by a remote, helping to ensure the opener will respond regardless of nearby frequency interference; features \"Intellicode\" rolling code technology that prevents unauthorized persons from opening the garage door by automatically changing the access code every time it is used; for faster installation, remotes are pre-programmed and are ready to use out-of-the-box, which eliminates one step during the initial installation process; \"HomeLink\" and \"Car2U\" compatibility links to car's built-in remote system;  includes (2) 3-button pre-programmed remotes,a  multi-function wall console with vacation lock and light control button and a self diagnostic Safe-T-Beam system."))));
	}
}
