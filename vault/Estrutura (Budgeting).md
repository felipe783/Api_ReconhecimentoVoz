# Estrutura do Projeto

```text
budgeting/
	📁 src/main/java/dio/budgeting
	|  |--BudgetingApplication.java      ← main + bean ChatClient
	|  |--ChatClientController.java      ← GET /api/chat
	|  |--ChatModelController.java       ← GET /api/chat-model
	|  |--TranscriptionController.java   ← POST /api/transcribe
	|  |--TextToSpeechController.java    ← POST /api/sinthesize
	|  |--📁 domain
	|  |  |--Category.java / Transaction.java / TransactionId.java / TransactionRepository.java
	|  |--📁 aplication
	|  |  |--ListTransactionByCategoryUseCase.java / PersistTransactionUseCase.java
	|  |  |--📁 input  ← PersistTransactionInput
	|  |  |--📁 output ← TransactionOutput
	|  |--📁 infrastructer
	|     |--📁 http         ← TransitonController, request/, response/
	|     |--📁 persistence  ← entity/TransactionEntity, repository/
	📁 src/main/resources
	|  |--application.properties
	|  |--📁 prompts
	|     |--system.st                   ← system prompt da assistente
	📁 src/test
	|  |--📁 java/dio/budgeting          ← 6 classes de teste
	|  |--📁 resources/audio             ← record-1.m4a, record-2.m4a
	📁 vault                             ← esta documentação (Obsidian); índice em _Budgeting
	|--build.gradle / settings.gradle / gradlew / gradlew.bat
	|--.env                              ← OPENAI_API_KEY (não versionado)
	|--HELP.md                           ← gerado pelo Spring Initializr
	|--README.md
```
## Pastas

- `domain/`: ver [[!Modulo Domain (Budgeting)]].
- `aplication/`: ver [[!Modulo Application (Budgeting)]].
- `infrastructer/`: ver [[!Modulo Infraestrutura (Budgeting)]].
- controllers da raiz e `resources/`: ver [[!Modulo IA (Budgeting)]].
