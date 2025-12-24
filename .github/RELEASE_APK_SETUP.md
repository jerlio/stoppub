# GitHub Action - Release APK Setup

## Configuration des Secrets

Pour que la GitHub Action puisse signer et publier l'APK de release, vous devez configurer les secrets suivants dans les paramètres du repository :

### Secrets Requis

1. **KEYSTORE_BASE64** : Votre fichier keystore encodé en base64
   ```bash
   base64 -i votre-keystore.jks | tr -d '\n'
   ```

2. **KEYSTORE_PASSWORD** : Le mot de passe du keystore

3. **KEY_ALIAS** : L'alias de la clé dans le keystore

4. **KEY_PASSWORD** : Le mot de passe de la clé

### Comment configurer les secrets

1. Allez dans les paramètres du repository GitHub : `Settings` > `Secrets and variables` > `Actions`
2. Cliquez sur `New repository secret`
3. Ajoutez chaque secret avec son nom et sa valeur correspondante

### Génération d'un Keystore (si vous n'en avez pas)

```bash
keytool -genkey -v -keystore stoppub.jks -keyalg RSA -keysize 2048 -validity 10000 -alias stoppub
```

Suivez les instructions et notez bien tous les mots de passe et l'alias que vous utilisez.

## Utilisation

Une fois les secrets configurés, la GitHub Action se déclenche automatiquement lorsque vous créez un tag de version :

```bash
git tag v1.0.0
git push origin v1.0.0
```

L'action va :
1. Builder l'APK de release
2. Signer l'APK avec votre keystore
3. Créer une release GitHub avec le tag
4. Attacher l'APK signé à la release

## APK Non Signé

Si les secrets ne sont pas configurés, l'action construira quand même un APK de release non signé. Cependant, cet APK ne pourra pas être installé sur un téléphone Android sans mode développeur activé.

## Téléchargement

Après chaque release, vous pourrez télécharger l'APK directement depuis la page des releases du repository : `https://github.com/jerlio/stoppub/releases`
