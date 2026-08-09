// for sending message to telegram
def call(String message, String token, String chatId){
    // read the value from credentials ,so that can just send with the message
    sh """
     curl -s -X POST "https://api.telegram.org/bot${token}/sendMessage" \
        -d chat_id="${chatId}" \
        -d parse_mode="Markdown"  \
        -d text="${message}"
    """
}