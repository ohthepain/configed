package com.config.configed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@SpringBootApplication
@RestController
public class ConfigedApplication {

	private enum Status {
        Active, Suspended, Decommissioned
    }

	class ConfigRecord {
		private String memberName;
		private int maxConnections;
		private Status status;

		ConfigRecord(String memberName, int maxConnections, Status status) {
			this.memberName = memberName;
			this.maxConnections = maxConnections;
			this.status = status;
		}

		public String getMemberName()
		{
			return memberName;
		}

		public int getMaxConnections()
		{
			return maxConnections;
		}

		public Status getStatus()
		{
			return status;
		}

		public void setMaxConnections(int maxConnections)
		{
			this.maxConnections = maxConnections;
		}

		public void setStatus(Status status)
		{
			this.status = status;
		}
	}

	private List<ConfigRecord> configRecords;

	ConfigedApplication()
	{
		this.configRecords = new ArrayList<ConfigRecord>();
	}

	public static void main(String[] args) {
		SpringApplication.run(ConfigedApplication.class, args);
	}

	@GetMapping("/records")
	public ResponseEntity<List<ConfigRecord>> list()
	{
		return ResponseEntity.ok(configRecords);
	}

    @PostMapping("/record")
    public ResponseEntity<String> postRecord(
									@RequestParam(value = "memberName", required=true) String memberName
									, @RequestParam(value = "maxConnections", required=true) int maxConnections
									, @RequestParam(value = "status", required=true) Status status)
	{
		if (!isValidMemberName(memberName))
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Illegal member name");
		}

		if (maxConnections < 0 || maxConnections > 32)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Illegal value for maxConnections");
		}

		for (ConfigRecord configRecord : configRecords)
		{
			if (configRecord.getMemberName().equals(memberName))
			{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate member name");
			}
		}

		ConfigRecord configRecord = new ConfigRecord(memberName, maxConnections, status);
		configRecords.add(configRecord);

		return ResponseEntity.ok("Successfully added record");
    }


    @PutMapping("/record")
    public ResponseEntity<String> putRecord(
									@RequestParam(value = "memberName", required=true) String memberName
									, @RequestParam(value = "maxConnections", required=false) Integer maxConnections
									, @RequestParam(value = "status", required=false) Status status)
	{
		if (!isValidMemberName(memberName))
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Illegal member name");
		}

		if (maxConnections != null && (maxConnections < 0 || maxConnections > 32))
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Illegal value for maxConnections");
		}

		for (ConfigRecord configRecord : configRecords)
		{
			if (configRecord.getMemberName().equals(memberName))
			{
				if (maxConnections != null)
				{
					configRecord.setMaxConnections(maxConnections);
				}

				if (status != null)
				{
					configRecord.setStatus(status);
				}

				return ResponseEntity.ok("Successfully updated member");		
			}
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found: " + memberName);
    }

	@GetMapping("/create")
    public ResponseEntity<String> createConfigValue(
									@RequestParam(value = "memberName", required=true) String memberName
									, @RequestParam(value = "maxConnections", required=true) int maxConnections
									, @RequestParam(value = "status", required=true) Status status)
	{
		if (!isValidMemberName(memberName))
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Illegal member name");
		}

		if (maxConnections < 0 || maxConnections > 32)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Illegal value for maxConnections");
		}

		configRecords.add(new ConfigRecord(memberName, maxConnections, status));

		return ResponseEntity.ok(String.format("Hello %s, maxConnections is %d, status is %s!", memberName, maxConnections, status));
    }

	private boolean isValidMemberName(String memberName) {
        String regex = "^[a-z0-9]+([-\\.][a-z0-9]+)*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(memberName);
        return matcher.matches();
    }
}
