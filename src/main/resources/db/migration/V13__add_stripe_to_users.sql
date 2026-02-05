ALTER TABLE app_users ADD COLUMN stripe_customer_id VARCHAR(255);
ALTER TABLE app_users ADD COLUMN stripe_subscription_id VARCHAR(255);