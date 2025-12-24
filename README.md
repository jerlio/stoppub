# StopPub

Application Android pour bloquer les appels publicitaires.

## 🚀 Releases APK

Ce projet dispose d'une GitHub Action qui génère automatiquement des APK de release signés lorsqu'un tag de version est créé.

### Créer une nouvelle release

Pour créer une nouvelle release, suivez ces étapes :

```bash
# Créer et pousser un tag de version
git tag v1.0.0
git push origin v1.0.0
```

La GitHub Action va automatiquement :
1. 📦 Builder l'APK de release
2. ✍️ Signer l'APK (si les secrets sont configurés)
3. 🎉 Créer une release GitHub avec le tag
4. 📎 Attacher l'APK à la release

### Télécharger et installer

1. Allez sur la [page des releases](../../releases)
2. Téléchargez le fichier APK de la dernière version
3. Installez l'APK sur votre téléphone Android

> ⚠️ **Note** : Vous devrez peut-être activer l'installation depuis des sources inconnues dans les paramètres de votre téléphone.

## 🔐 Configuration du Signing (pour les mainteneurs)

Pour que les APK soient signés automatiquement, vous devez configurer les secrets suivants dans le repository :

Voir [.github/RELEASE_APK_SETUP.md](.github/RELEASE_APK_SETUP.md) pour les instructions détaillées.

## 🛠️ Développement

### Prérequis

- Android Studio
- JDK 17 ou supérieur
- Android SDK

### Build local

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease
```

## 📝 License

Ce projet est sous licence libre.
