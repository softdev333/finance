package com.mb.finance.controller;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mb.finance.config.ConversionRequest;
import com.mb.finance.config.ExpenseDto;
import com.mb.finance.config.ExpenseType;
import com.mb.finance.config.IncomeDto;
import com.mb.finance.config.UserRegistrationDto;
import com.mb.finance.entities.BankAccount;
import com.mb.finance.entities.Expense;
import com.mb.finance.entities.FinanceUser;
import com.mb.finance.entities.Income;
import com.mb.finance.service.BankAccountService;
import com.mb.finance.service.ExpenseService;
import com.mb.finance.service.FinanceUserService;
import com.mb.finance.service.IncomeService;
import com.mb.finance.service.UserService;
import com.mb.finance.websecurity.AuthRequest;
import com.mb.finance.websecurity.JwtService;

import jakarta.xml.bind.DatatypeConverter;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MainController {

	@Autowired
	UserService userService;

	@Autowired
	FinanceUserService financeUserService;

	@Autowired
	IncomeService incomeService;

	@Autowired
	ExpenseService expenseService;

	@Autowired
	BankAccountService bankAccountService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	//test apis
	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome this endpoint is not secure";
	}

	//test apis
	@GetMapping("/user/userProfile")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String userProfile() {
		return "Welcome to User Profile";
	}

	//test apis
	@GetMapping("/admin/adminProfile")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String adminProfile() {
		return "Welcome to Admin Profile";
	}

	@PostMapping("/user")
	public ResponseEntity<Map<String, Object>> saveUser(@RequestBody UserRegistrationDto request)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		userService.saveUser(request);
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("message", "save successful");
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatusCode.valueOf(200));

	}

	@PostMapping("/generateToken")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getUsername());
		} else {
			throw new UsernameNotFoundException("Invalid user request!");
		}
	}

	/*
	 * @PostMapping("/login") public ResponseEntity<Map<String, Object>>
	 * login(@RequestBody LoginDto request) throws NoSuchAlgorithmException,
	 * InvalidKeySpecException {
	 * 
	 * Boolean loginSuccesssful = userService.login(request);
	 * 
	 * Map<String, Object> response = new HashMap<String, Object>(); FinanceUser
	 * user = financeUserService.getUserByUserId(request.getUserId()).get();
	 * response.put("firstName", user.getFirstName()); response.put("lastName",
	 * user.getLastName()); response.put("userId", user.getUserId());
	 * response.put("email", user.getEmail());
	 * 
	 * Map<String, Object> responseMap = new HashMap<String, Object>(); if
	 * (loginSuccesssful != null) { responseMap.put("message", "login successful");
	 * return new ResponseEntity<Map<String, Object>>(responseMap,
	 * HttpStatusCode.valueOf(200)); } else { responseMap.put("message",
	 * "login unsuccessful"); return new ResponseEntity<Map<String,
	 * Object>>(responseMap, HttpStatusCode.valueOf(401)); }
	 * 
	 * }
	 */

	@GetMapping("/user")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Map<String, Object>> getUserProfile(@RequestParam String userId) {
		Map<String, Object> response = new HashMap<String, Object>();
		FinanceUser user = financeUserService.getUserByUserId(userId).get();
		response.put("firstName", user.getFirstName());
		response.put("lastName", user.getLastName());
		response.put("userId", user.getUserId());
		response.put("email", user.getEmail());

		return ResponseEntity.ok(response);
	}

	@PostMapping("/income")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Map<String, Object>> saveIncome(@RequestBody IncomeDto incomeDto) throws Exception {
		userService.addIncome(incomeDto);

		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("message", "save successful");
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatusCode.valueOf(200));
	}

	@GetMapping("/user/income/all")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Map<String, Object>> getAllIncome(@RequestParam String userId) throws Exception {
		List<Income> incomes = incomeService.getAllIncomeByUserId(userId);
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("incomes", incomes);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatusCode.valueOf(200));
	}

	@GetMapping("/user/income/current-month")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<BigDecimal> getAllIncomeCurrentMonth(@RequestParam String userId) throws Exception {

		BigDecimal incomeCurrentMonth = incomeService.getAllIncomeForCurrentMonth(userId, LocalDate.now());

		return new ResponseEntity<BigDecimal>(incomeCurrentMonth, HttpStatusCode.valueOf(200));
	}

	@PostMapping("/expense")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Map<String, Object>> saveExpense(@RequestBody ExpenseDto expenseDto) throws Exception {
		userService.addExpense(expenseDto);

		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("message", "save successful");
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatusCode.valueOf(200));
	}

	@PostMapping("/convert")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Map<String, Object>> conversion(@RequestBody ConversionRequest conversionRequest)
			throws Exception {
		userService.convert(conversionRequest);

		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("message", "convert successful");
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatusCode.valueOf(200));
	}

	/*
	 * @GetMapping("/user/expense/all") public ResponseEntity<Map<String, Object>>
	 * getAllExpense(@RequestParam String userId) throws Exception { List<Expense>
	 * expenses = expenseService.getExpensesByUserId(userId); Map<String, Object>
	 * responseMap = new HashMap<String, Object>(); responseMap.put("expenses",
	 * expenses); return new ResponseEntity<Map<String, Object>>(responseMap,
	 * HttpStatusCode.valueOf(200)); }
	 */

	@GetMapping("/user/expense/all")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<List<Expense>> getAllExpense(@RequestParam String userId) throws Exception {
		List<Expense> expenses = expenseService.getExpensesByUserId(userId);
		return new ResponseEntity<List<Expense>>(expenses, HttpStatusCode.valueOf(200));
	}

	@GetMapping("/user/expense/current-month")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<BigDecimal> getAllExpenseCurrentMonth(@RequestParam String userId) throws Exception {
		return new ResponseEntity<BigDecimal>(
				expenseService.getAllExpensesForCurrentMonthForUser(userId, LocalDate.now()),
				HttpStatusCode.valueOf(200));
	}

	@PostMapping("/user/expense/current-month/exclude")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<BigDecimal> getAllExpenseCurrentMonthExcept(@RequestParam String userId,
			@RequestBody Map<String, Object> requestMap) throws Exception {

		List<String> expenseTypeString = (List<String>) requestMap.get("exclude");

		List<ExpenseType> expenseTypes = expenseTypeString.stream().map(ExpenseType::valueOf)
				.collect(Collectors.toList());

		BigDecimal expense = expenseService.getExpensesForCurrentMonthExcept(userId, LocalDate.now(), expenseTypes);

		return new ResponseEntity<BigDecimal>(expense, HttpStatusCode.valueOf(200));
	}

	@GetMapping("/user/expense/total")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<BigDecimal> getTotalExpense(@RequestParam String userId) throws Exception {
		return new ResponseEntity<BigDecimal>(expenseService.getTotalExpenseByUserId(userId),
				HttpStatusCode.valueOf(200));
	}

	@PostMapping("/bankaccount")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Map<String, Object>> saveBankAccount(@RequestBody BankAccount bankAccount) throws Exception {

		bankAccountService.saveBankAccount(bankAccount);
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("message", "save successful");
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatusCode.valueOf(200));
	}

	@GetMapping("/user/bankaccount/all")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Map<String, Object>> getAllBankAccount(@RequestParam String userId) throws Exception {
		List<BankAccount> bankAccounts = bankAccountService.getAllAccountsForUserId(userId);
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("bankAccounts", bankAccounts);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatusCode.valueOf(200));
	}

	@PostMapping("/user/bankaccount")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Map<String, Object>> getBankAccount(@RequestParam String userId,
			@RequestBody Map<String, Object> requestMap) throws Exception {

		String accountNumber = (String) requestMap.get("accountNumber");
		BankAccount bankAccount = bankAccountService.findByUserIdAndAccountNumber(userId, accountNumber);

		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("bankAccount", bankAccount);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatusCode.valueOf(200));
	}

	@GetMapping("/user/balance/current-month")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Map<String, Object>> getCurrentTotalBalance(@RequestParam String userId) throws Exception {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		BigDecimal currentMonthTotalBalance = userService.getBalanceCurrentMonth(userId);
		responseMap.put("balance", currentMonthTotalBalance);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatusCode.valueOf(200));
	}

	@GetMapping("/user/balance")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Map<String, Object>> getTotalBalance(@RequestParam String userId) throws Exception {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		BigDecimal totalBalance = userService.getTotalBalance(userId);
		responseMap.put("balance", totalBalance);
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatusCode.valueOf(200));
	}

	@GetMapping("/generate/db/key")
	public ResponseEntity<String> generateDBKey() {
		String generatedKeyString = "";

		try {
			// Create a KeyGenerator instance for AES
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");

			// Generate a random AES key with the desired key size (e.g., 128 bits)
			keyGen.init(256); // Specify the key size here
			SecretKey secretKey = keyGen.generateKey();

			// Convert the SecretKey to a byte array
			byte[] keyBytes = secretKey.getEncoded();

			// Convert the byte array to a hexadecimal string
			generatedKeyString = DatatypeConverter.printHexBinary(keyBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error generating AES key", e);
		}

		return ResponseEntity.ok(generatedKeyString);
	}

	@DeleteMapping("/user")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<Map<String, Object>> deleteIncomeOrExpense(@RequestBody Map<String, Object> requestMap)
			throws Exception {
		String userId = (String) requestMap.get("userId");
		String transactionType = (String) requestMap.get("transactionType");
		if ("expense".equals(transactionType)) {
			userService.deleteExpense(userId, requestMap);
		} else if ("income".equals(transactionType)) {
			userService.deleteIncome(userId, requestMap);
		}

		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("message", "delete successful");
		return new ResponseEntity<Map<String, Object>>(responseMap, HttpStatusCode.valueOf(200));
	}

}
