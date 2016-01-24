package com.dinenowinc.dinenow.resources;

import com.dinenowinc.dinenow.model.User;
import io.dropwizard.auth.Auth;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dinenowinc.dinenow.dao.AddressBookDao;
import com.dinenowinc.dinenow.dao.CustomerDao;
import com.dinenowinc.dinenow.error.ServiceErrorMessage;
import com.dinenowinc.dinenow.model.AddressBook;
import com.dinenowinc.dinenow.model.Customer;
import com.dinenowinc.dinenow.model.Restaurant;
import com.dinenowinc.dinenow.model.UserRole;
import com.dinenowinc.dinenow.validation.AddressBookValidator;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/address_book")
@Api("/address_book")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AddressBookResource extends AbstractResource<AddressBook>{

	@Inject
	private CustomerDao customerDao;
	@Inject
	private AddressBookDao addressBookDao;
	
	@Override
	protected HashMap<String, Object> fromEntity(AddressBook entity) {
		HashMap<String, Object> dto = new HashMap<String, Object>();
		dto.put(getClassT().getSimpleName().toLowerCase(), entity.toDto());
		System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::");
		return dto;
	}
	
	@Override
	protected AddressBook fromAddDto(HashMap<String, Object> dto) {
		AddressBook addressBook = super.fromAddDto(dto);
		addressBook.setName(dto.get("name").toString());
		addressBook.setAddress1(dto.get("address_1").toString());
		addressBook.setAddress2(dto.get("address_2").toString());
		addressBook.setCity(dto.get("city").toString());
		addressBook.setProvince(dto.get("province").toString());
		addressBook.setCountry(dto.get("country").toString());
		addressBook.setPostalCode(dto.get("postal_code").toString());
		addressBook.setDeliveryInstructions(dto.get("delivery_instructions").toString());
	//	HashMap<String, Double> location = (HashMap<String, Double>)dto.get("location");
	//	LatLng latlng = new LatLng(location.get("lat"), location.get("lng"));
	//	addressBook.setLocation(latlng);
		
		return addressBook;
	}
	
	@Override
	protected Response onAdd(User access, AddressBook entity, Restaurant restaurant) {
		
		Customer cus = customerDao.findOne(access.getId().toString());
		dao.save(entity);
		cus.addAddressBook(entity);
		customerDao.update(cus);
		return ResourceUtils.asSuccessResponse(Status.OK, fromEntity(entity));
	}
	
	
	
	@Override
	protected HashMap<String, Object> onGet(AddressBook entity) {
		return super.onGet(entity);
	}
	
	
	@Override
	protected AddressBook fromUpdateDto(AddressBook t, HashMap<String, Object> dto) {
		AddressBook addressBook = super.fromUpdateDto(t, dto);
		addressBook.setName(dto.get("name").toString());
		addressBook.setAddress1(dto.get("address_1").toString());
		addressBook.setAddress2(dto.get("address_2").toString());
		addressBook.setCity(dto.get("city").toString());
		addressBook.setProvince(dto.get("province").toString());
		addressBook.setCountry(dto.get("country").toString());
		addressBook.setPostalCode(dto.get("postal_code").toString());
		addressBook.setDeliveryInstructions(dto.get("delivery_instructions").toString());
	//	HashMap<String, Double> location = (HashMap<String, Double>)dto.get("location");
	//	LatLng latlng = new LatLng(location.get("lat"), location.get("lng"));
    //	addressBook.setLocation(latlng);
		
		return addressBook;
	}
	
	
	
	
	
	@GET
	@ApiOperation(value = "api get all address book", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data")
			})
	public Response getAll(@ApiParam(access = "internal") @Auth User access) {
		/*List<AddressBook> entities = addressBookDao.getByCustomer(access.getId().toString());
		List<HashMap<String, Object>> dtos = fromEntities(entities);
		return ResourceUtils.asSuccessResponse(Status.OK, dtos);*/
		return super.getAll(access);
	}
		
	@GET
	@Path("/{id}")
	@ApiOperation(value = "api get detail address book")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data"),
			@ApiResponse(code = 404, message = "Cannot found entity") 
			})
	@Override
	public Response get(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
		return super.get(access, id);
	}
	
	
	
	
	@POST
	@ApiOperation(value = "api add new address book", notes="{"
			+ "<br/>  \"address\": \"51 hoang viet\","
			+ "<br/>  \"postalCode\": \"70000\","
			+ "<br/>  \"deliveryInstructions\": \"vao ngo 50m\","
			+ "<br/>  \"location\": {"
			+ "<br/>    \"lat\": 10.106,"
			+ "<br/>    \"lng\": 106.106"
			+ "<br/>  }"
			+ "<br/>}")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data") 
			})
	@Override
	public Response add(@ApiParam(access = "internal") @Auth User access, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.CUSTOMER) {
			AddressBookValidator typeValidator = new AddressBookValidator(dto);
			List<ServiceErrorMessage> mListError = typeValidator.validateForAdd();
			if (mListError.size() == 0) {
				return super.add(access, dto);
			}
			return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, mListError);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED,new ServiceErrorMessage("Only for user"));
	}
	
	
	@PUT
	@ApiOperation(value = "api update address book")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "data")
			})
	@Path("/{id}")
	@Override
	public Response update(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id, HashMap<String, Object> dto) {
		if (access.getRole() == UserRole.CUSTOMER) {
				AddressBookValidator typeValidator = new AddressBookValidator(dto);
				List<ServiceErrorMessage> mListError = typeValidator.validateForAdd();
				if (mListError.size() == 0) {
					return super.update(access, id, dto);
				}
				return ResourceUtils.asFailedResponse(Status.BAD_REQUEST, mListError);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Only for user"));
	}
	
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "api delete address book")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 401, message = "access denied for user")
			})
	@Override
	public Response delete(@ApiParam(access = "internal") @Auth User access, @PathParam("id") String id) {
		if (access.getRole() == UserRole.CUSTOMER) {
			return super.delete(access, id);
		}
		return ResourceUtils.asFailedResponse(Status.UNAUTHORIZED, new ServiceErrorMessage("Only for user"));
	}	
}
