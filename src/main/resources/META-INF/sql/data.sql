-- Insert roles
INSERT INTO `roles` (`name`, `rank`) VALUES ('user', 1),  ('admin', 100), ('teacher', 3), ('parent', 2);

-- Insert default image
INSERT INTO `pictures` (`file_location`) VALUES ('default.png');

-- Insert default users
INSERT INTO `users` (`username`, `password`, `gender`, `disabled`, `role_id`) VALUES ('admin', '$31$16$Gg3Z3qa0KO9kNQ5V-R87Nz9-KN_j8y7b0SCFIEWPsMk', 0, 0, 2);
