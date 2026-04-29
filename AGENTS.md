# 🤖 AI Agents Collaboration Guide (agents.md)

## 🎯 Goal

Efficiently implement features end-to-end in a pair programming setup using AI tools, while maintaining clean architecture and shared understanding.

---

## 👥 Team Setup

We work in **parallel with AI support**, not sequentially.

- Both developers:
  - Work on separate parts simultaneously
  - Use AI tools independently
  - Sync frequently (every ~5–10 min)

---

## 🧠 Core Principles

1. **Architecture first**
   - Follow: UI → ViewModel → Repository → Data Source (API/DB)
   - Never mix responsibilities

2. **Small steps**
   - Implement in small, testable chunks

3. **AI = assistant, not authority**
   - Always verify AI output
   - Prefer understanding over copying

4. **Consistency > speed**
   - Naming, structure, patterns must match

---

## 🤖 Agent Roles

We use AI in structured roles:

---

### 🏗️ Architect Agent

**Purpose:** Plan before coding

**Prompt example:**

> "Given this task, design the architecture using ViewModel, Repository, and Retrofit. Show file structure."

**Responsibilities:**

- Define classes and responsibilities
- Suggest data flow
- Ensure clean architecture

---

### 💻 Implementation Agent

**Purpose:** Generate code

**Prompt example:**

> "Implement a Hilt-injected ViewModel that fetches data from a repository using Flow."

**Responsibilities:**

- Write Kotlin code
- Follow architecture
- Use:
  - `@HiltViewModel`
  - `@Inject`
  - Retrofit API interfaces
  - Flow / coroutines

---

### 🧪 Debugging Agent

**Purpose:** Fix errors quickly

**Prompt example:**

> "Explain this crash and suggest minimal fixes: [paste error]"

**Responsibilities:**

- Analyze stack traces
- Suggest minimal fixes
- Avoid rewriting everything

---

### 🔍 Review Agent

**Purpose:** Validate correctness

**Prompt example:**

> "Check if this follows clean architecture and best practices."

**Responsibilities:**

- Check:
  - Separation of concerns
  - Correct DI usage
  - Flow usage

- Suggest improvements

---

## ⚙️ Work Split Strategy

We divide work by **layers**, not files:

### Developer A

- UI (Compose)
- Navigation
- State handling

### Developer B

- ViewModel
- Repository
- API / DB

➡️ Sync point:

- Define data models together first

---

## 🔄 Collaboration Workflow

1. **Understand task together (2–3 min)**
2. **Ask Architect Agent**
3. **Split work**
4. **Implement in parallel**
5. **Sync + integrate**
6. **Use Review Agent**
7. **Fix issues with Debug Agent**

---

## 📡 Communication Rules

- Always state:
  - what you are working on
  - what you expect from the other person

Example:

> "I’m implementing the ViewModel, I need the API response model from you."

---

## ⚠️ Common Pitfalls (Avoid These!)

❌ Generating huge code blocks without understanding
❌ Both people editing same file simultaneously
❌ Ignoring architecture
❌ Not syncing early → merge chaos
❌ Blindly trusting AI output

---

## 🧩 Android-Specific Rules

### Dependency Injection (Hilt)

- Use:
  - `@HiltAndroidApp` (Application)
  - `@AndroidEntryPoint` (Activity)
  - `@HiltViewModel`
  - `@Inject constructor`

### Networking (Retrofit)

- Define API interface
- Use suspend functions
- Inject via repository

### Coroutines & Flow

- Use `viewModelScope.launch`
- Use `Flow` for streams
- Collect in ViewModel

### Repository Pattern

- Repository decides:
  - API vs DB

- Never call API directly from UI

---

## 🚀 Final Strategy for Test

- Don’t aim for perfection → aim for **working feature**
- Prioritize:
  1. Working flow (UI → API → display)
  2. Clean structure
  3. No crashes

---

## 🧠 Mindset

> "We are not coding alone. We are coordinating humans + AI."

---

## ✅ Definition of Done

- Feature works end-to-end
- No crashes
- Architecture is respected
- Both teammates understand the solution
