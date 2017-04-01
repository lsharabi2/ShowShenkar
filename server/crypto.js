var passwordHash = require('password-hash');

    


exports.hashMake = function (text) {
	var hashedText = passwordHash.generate(text);
	return hashedText;

}

exports.hashCheck = function (text, hashedText) {
	
	var verify = passwordHash.verify(text, hashedText);
	return verify;
}
