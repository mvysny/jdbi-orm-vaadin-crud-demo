/* Since we allow the Grid to sort by age, alive, dob and marital status, we need to add indices on those columns as well */
create INDEX Person_age on Person (age);
create INDEX Person_dob on Person (dateOfBirth);
create INDEX Person_alive on Person (alive);
create INDEX Person_maritalStatus on Person (maritalStatus);
