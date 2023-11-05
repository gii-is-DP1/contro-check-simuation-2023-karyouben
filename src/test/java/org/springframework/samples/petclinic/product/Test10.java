package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class Test10 extends ReflexiveTest {
	ProductRepository pr;

	ProductService ps;

	public static Integer A_PRODUCT_ID = 1;

	@BeforeEach
	void createService() {
		pr = mock(ProductRepository.class);
		ps = new ProductService(pr);
	}

	@Test
	public void testUpdates() {
		int data[][] = {
				{ 1, 1, 0 },
				{ 2, 1, 0 },
				{ 2, 2, 0 },
				{ 2, 3, 0 },
				{ 2, 4, 0 },
				{ 2, 5, 1 },
				{ 5, 20, 1 }
		};
		boolean unfeasible=false;
		for (int[] mydata : data) {
			Product newProduct = Test7.createProduct();
			Product oldProduct = Test7.createProduct();
			Class[] paramTypes = { Integer.class };
			if (classHasMethod(oldProduct, "setId", paramTypes)) {
				invokeMethodReflexivelyWithParamTypes(oldProduct, "setId", paramTypes, 5);
				invokeMethodReflexivelyWithParamTypes(newProduct, "setId", paramTypes, 5);
			}
			newProduct.setPrice(mydata[1]);
			oldProduct.setPrice(mydata[0]);
			unfeasible= mydata[2]==1;
			if (unfeasible)
				testUpdateUnfeasible(oldProduct, newProduct);
			else
				testUpdateOk(oldProduct, newProduct);
		}
	}

	private void testUpdateUnfeasible(Product oldProduct, Product newProduct) {
		reset(pr);
		when(pr.findById(any(Integer.class))).thenReturn(Optional.of(oldProduct));
		assertThrows(UnfeasibleProductUpdate.class, () -> ps.save(newProduct));
	}

	private void testUpdateOk(Product oldProduct, Product newProduct) {
		reset(pr);
		when(pr.findById(any(Integer.class))).thenReturn(Optional.of(oldProduct));
		try {
			ps.save(newProduct);
			verify(pr,times(1)).save(newProduct);
		} catch (UnfeasibleProductUpdate e) {
			fail("No exception should be thrown! " + e.getStackTrace());
		}

	}
}