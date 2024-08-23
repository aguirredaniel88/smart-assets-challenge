conn = new Mongo();
db = conn.getDB("smart-assets");

db.createCollection("brand");
db.createCollection("campaign");
db.createCollection("creative");

db.campaign.createIndex({brandId: 1}, {unique: false});
db.creative.createIndex({campaignId: 1}, {unique: false});

db.createUser(
        {
            user: "smart-assets",
            pwd: "my-password",
            roles: [
                {
                    role: "readWrite",
                    db: "smart-assets"
                }
            ]
        }
);

