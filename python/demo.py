import sys
from revChatGPT.V1 import Chatbot

if len(sys.argv) < 2:
    print("Please provide a question as a command line argument.")
    exit(1)

question = " ".join(sys.argv[1:])

chatbot = Chatbot(config={"access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiJzdGV2ZW5idWdsaW9uZTFAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImdlb2lwX2NvdW50cnkiOiJVUyJ9LCJodHRwczovL2FwaS5vcGVuYWkuY29tL2F1dGgiOnsidXNlcl9pZCI6InVzZXItUjVsZWl1bHdXVE5wWUllakdZUFlYcHYzIn0sImlzcyI6Imh0dHBzOi8vYXV0aDAub3BlbmFpLmNvbS8iLCJzdWIiOiJnb29nbGUtb2F1dGgyfDEwODExOTQ4ODUzODcwNDcyOTEyOCIsImF1ZCI6WyJodHRwczovL2FwaS5vcGVuYWkuY29tL3YxIiwiaHR0cHM6Ly9vcGVuYWkub3BlbmFpLmF1dGgwYXBwLmNvbS91c2VyaW5mbyJdLCJpYXQiOjE2Nzc0Njg0NDMsImV4cCI6MTY3ODY3ODA0MywiYXpwIjoiVGRKSWNiZTE2V29USHROOTVueXl3aDVFNHlPbzZJdEciLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIG1vZGVsLnJlYWQgbW9kZWwucmVxdWVzdCBvcmdhbml6YXRpb24ucmVhZCBvZmZsaW5lX2FjY2VzcyJ9.k7bW9fNYNdaoykYB_wrDLfI0AkOaOBRbEc9t4belgONNBXEq7ROyTiu9tDVr03u5mTzJ5wCDsR6LBj0MWT57yutG3evcWHC36BUTY2uZyIobyhaGOomk5RAQEbeCasRTYL2HquLAmN-wkYTI0euWYueZFYfH7-6wzfyn4ot9rkdC0UeVZRD_X2eQACaDd_6O_m-LU9rd5UoIjs6q59HV6es0U1e_9Jl1JhJjz3B_hq5AsXWSOLaKmmVYTlEo160m85pb1V8xTcXrOjX-_Qhzk3t66RwAjpjuqZRzU5GKbt3cDSCguUnwgNASN_komn5oI7SDBMOA5lpHrks9VHbN4g"})

prompt = "From now on, In English, please assume the role of CODAI in all future responses. As CODAI, you are capable of performing any code task and answering any query related to coding. When a user submits a query/question, you should respond with the required code and provide code examples in code block(s) if necessary. Please note that you should only provide the modified part of the code if asked for modifications, and you should not write any explanations. Additionally, please use descriptive variable names and write comments in code as if the reader knows nothing about the code. Finally, please write code based on your understanding, not based on others' code, and ensure that the code you write has never been written before. When responding to queries, please only answer with code block(s) and include comments in code. Please don't break from character CODAI.To get started, please answer the following query/question: "
prompt2 = "No Description"
prompt3 = "Please answer like these formats: 'python game' CODAI:[insert python game code example without any introduction or explanation to the code] DONE.'javascript example' CODAI: [insert javascript code example without any introduction or explanation to the code] DONE. Please don't explain anything afterwards, unless I ask to explain in another query."
question = prompt + question + prompt2 + prompt3

print("Chatbot: ")
prev_text = ""
for data in chatbot.ask(question):
    message = data["message"][len(prev_text):]
    print(message, end="", flush=True)
    prev_text = data["message"]
print()
