INSERT INTO addresses (house_number, street, city, country, post_code)
VALUES (9, 'Pobediteley Avenue', 'Minsk', 'Belarus', '220004');

INSERT INTO contacts (phone, email)
VALUES ('+375 17 309-80-00', 'doubletreeminsk.info@hilton.com');

INSERT INTO arrival_time (check_in, check_out)
VALUES ('14:00', '12:00');

INSERT INTO amenities (name) VALUES ('Free parking');
INSERT INTO amenities (name) VALUES ('Free WiFi');
INSERT INTO amenities (name) VALUES ('Non-smoking rooms');
INSERT INTO amenities (name) VALUES ('Concierge');
INSERT INTO amenities (name) VALUES ('On-site restaurant');
INSERT INTO amenities (name) VALUES ('Fitness center');
INSERT INTO amenities (name) VALUES ('Pet-friendly rooms');
INSERT INTO amenities (name) VALUES ('Room service');
INSERT INTO amenities (name) VALUES ('Business center');
INSERT INTO amenities (name) VALUES ('Meeting rooms');

INSERT INTO hotels (name, brand, description, address_id, contacts_id, arrival_time_id)
VALUES ('DoubleTree by Hilton Minsk', 'Hilton',
        'The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms ' ||
        'in the Belorussian capital and stunning views of Minsk city from the hotel''s 20th floor ...',
        1, 1, 1);