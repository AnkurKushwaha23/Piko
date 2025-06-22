# 🖼️ Piko

Piko is a lightweight, minimal image loading library for Android inspired by Glide and Picasso. It helps you load images efficiently into your `ImageView` with support for caching, placeholders.

---

## ✍️ A Note from the Author

> This library is not perfect — in fact, it’s far from it.  
> I built **Piko** out of curiosity after diving into the system design behind image loading libraries.  
> As someone who has been using **Glide** since the beginning of my Android development journey, I wanted to challenge myself to build something from scratch and truly understand how it works under the hood.  
>
> While building this, I learned a lot — especially about request handling, caching, and decoding. A big thanks to **ChatGPT** for helping me debug, understand concepts in depth, and stay motivated throughout the process.  
>
> Again, this library still needs a lot of improvement, but it’s a start and I hope it inspires others to explore and build too.

---

## 🚀 Features

- ✅ Load images from URLs
- 🌀 Placeholder and error image support
- 🧠 In-memory and disk caching
- 🧩 Clean, chainable API
- 🛠️ Easy to extend/customize

---

## ⚙️ Tech Stack

- **Kotlin**
- **Coroutines**
- **OkHttp** (for image downloading)
- **LruCache** (for memory caching)
- **DiskLruCache** by [Jake Wharton](https://github.com/JakeWharton/DiskLruCache) (for disk caching)

---

## 📦 Installation

- Step 1. Add the JitPack repository to your build file 

Add it in your `settings.gradle.kts` at the end of repositories:

```gradle.kts
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}
```
- Step 2. Add the Piko dependency to your app-level `build.gradle` :

```
	dependencies {
	        implementation("com.github.AnkurKushwaha23:Piko:Tag")
	}
```

## 🔧 Usage

- Basic Usage
  
```
  Piko.with(context)
    .load("https://example.com/image.jpg")
    .into(imageView)
```
- With Placeholder and Error

```
  Piko.with(context)
    .load("https://example.com/image.jpg")
    .placeholder(R.drawable.placeholder)
    .error(R.drawable.error)
    .into(imageView)
```

## 🤝 Contributing

Contributions are welcome! Feel free to fork this repository and submit a pull request. Please open an issue first to discuss changes.

## 📄 License

```
MIT License

Copyright (c) 2025 Ankur Kushwaha

Permission is hereby granted, free of charge, to any person obtaining a copy...
```
