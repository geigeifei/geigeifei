#include "PlagiarismChecker.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include <cmath>
#include <algorithm>
#include <iomanip>
#include <codecvt>
#include <locale>

using namespace std;

// ���캯������ȡԭ�ĺͳ�Ϯ�����ĵ�����
PlagiarismChecker::PlagiarismChecker(const string& origFile, const string& plagFile) {
	origText = readFile(origFile);  // ��ȡԭ��
	plagText = readFile(plagFile);  // ��ȡ��Ϯ������
}

// ��ȡUTF-8������ļ�
string PlagiarismChecker::readFile(const string& filePath) {
	wifstream wif(filePath);
	wif.imbue(locale(locale(), new codecvt_utf8<wchar_t>)); // ����UTF-8����
	wstringstream wss;
	wss << wif.rdbuf();
	wstring wstr = wss.str();

	// ��wstringת��Ϊstring��UTF-8��
	wstring_convert<codecvt_utf8<wchar_t>> converter;
	return converter.to_bytes(wstr);
}

// ���ַ��ִ�
vector<string> PlagiarismChecker::tokenize(const string& text) {
	vector<string> tokens;
	for (char ch : text) {
		if (ch != ' ' && ch != '\n' && ch != '\r') { // ���Կո�ͻ��з�
			tokens.push_back(string(1, ch));
		}
	}
	return tokens;
}

// �����Ƶ��TF��
map<string, int> PlagiarismChecker::computeTF(const vector<string>& words) {
	map<string, int> tf;
	for (const string& word : words) {
		tf[word]++;
	}
	return tf;
}

// �������ĵ�Ƶ�ʣ�IDF��
map<string, double> PlagiarismChecker::computeIDF(const vector<vector<string>>& documents) {
	map<string, double> idf;
	int totalDocs = documents.size();
	for (const auto& doc : documents) {
		for (const string& word : doc) {
			idf[word] = 0;
		}
	}
	for (const auto& doc : documents) {
		for (const string& word : doc) {
			idf[word]++;
		}
	}
	for (auto& pair : idf) {
		pair.second = log10(totalDocs / pair.second);
	}
	return idf;
}

// ����TF-IDF����
vector<double> PlagiarismChecker::computeTFIDF(const vector<string>& words,
	const map<string, int>& tf,
	const map<string, double>& idf) {
	vector<double> tfidf;
	for (const string& word : words) {
		tfidf.push_back(tf.at(word) * idf.at(word));
	}
	return tfidf;
}

// �����������ƶ�
double PlagiarismChecker::cosineSimilarity(const vector<double>& vec1, const vector<double>& vec2) {
	double dotProduct = 0.0, magnitude1 = 0.0, magnitude2 = 0.0;
	for (size_t i = 0; i < vec1.size(); ++i) {
		dotProduct += vec1[i] * vec2[i];
		magnitude1 += vec1[i] * vec1[i];
		magnitude2 += vec2[i] * vec2[i];
	}
	magnitude1 = sqrt(magnitude1);
	magnitude2 = sqrt(magnitude2);
	if (magnitude1 == 0 || magnitude2 == 0) {
		return 0.0;
	}
	return dotProduct / (magnitude1 * magnitude2);
}

// �������ƶȲ�������
void PlagiarismChecker::calculateAndSaveSimilarity(const string& outputFile) {
	// �ִ�
	vector<string> origTokens = tokenize(origText);
	vector<string> plagTokens = tokenize(plagText);

	// ����TF
	map<string, int> origTF = computeTF(origTokens);
	map<string, int> plagTF = computeTF(plagTokens);

	// ����IDF
	vector<vector<string>> documents = { origTokens, plagTokens };
	map<string, double> idf = computeIDF(documents);

	// ����TF-IDF����
	vector<double> origTFIDF = computeTFIDF(origTokens, origTF, idf);
	vector<double> plagTFIDF = computeTFIDF(plagTokens, plagTF, idf);

	// �����������ƶ�
	double similarity = cosineSimilarity(origTFIDF, plagTFIDF);

	// ���������ļ�
	ofstream outFile(outputFile);
	outFile << fixed << setprecision(2) << similarity << endl;
}

// �����������������в��������ò��ع���
int main(int argc, char* argv[]) {
	// ��������в�������
	if (argc != 4) {
		cerr << "Usage: " << argv[0] << " <original_file> <plagiarized_file> <output_file>" << endl;
		return 1;
	}

	try {
		// �������ض���
		PlagiarismChecker checker(argv[1], argv[2]);

		// �������ƶȲ�������
		checker.calculateAndSaveSimilarity(argv[3]);

		cout << "Similarity calculation completed successfully." << endl;
	}
	catch (const exception& e) {
		cerr << "Error: " << e.what() << endl;
		return 1;
	}

	return 0;
}