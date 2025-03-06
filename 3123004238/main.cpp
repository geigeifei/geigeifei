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

// 构造函数：读取原文和抄袭版论文的内容
PlagiarismChecker::PlagiarismChecker(const string& origFile, const string& plagFile) {
	origText = readFile(origFile);  // 读取原文
	plagText = readFile(plagFile);  // 读取抄袭版论文
}

// 读取UTF-8编码的文件
string PlagiarismChecker::readFile(const string& filePath) {
	wifstream wif(filePath);
	wif.imbue(locale(locale(), new codecvt_utf8<wchar_t>)); // 设置UTF-8编码
	wstringstream wss;
	wss << wif.rdbuf();
	wstring wstr = wss.str();

	// 将wstring转换为string（UTF-8）
	wstring_convert<codecvt_utf8<wchar_t>> converter;
	return converter.to_bytes(wstr);
}

// 按字符分词
vector<string> PlagiarismChecker::tokenize(const string& text) {
	vector<string> tokens;
	for (char ch : text) {
		if (ch != ' ' && ch != '\n' && ch != '\r') { // 忽略空格和换行符
			tokens.push_back(string(1, ch));
		}
	}
	return tokens;
}

// 计算词频（TF）
map<string, int> PlagiarismChecker::computeTF(const vector<string>& words) {
	map<string, int> tf;
	for (const string& word : words) {
		tf[word]++;
	}
	return tf;
}

// 计算逆文档频率（IDF）
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

// 计算TF-IDF向量
vector<double> PlagiarismChecker::computeTFIDF(const vector<string>& words,
	const map<string, int>& tf,
	const map<string, double>& idf) {
	vector<double> tfidf;
	for (const string& word : words) {
		tfidf.push_back(tf.at(word) * idf.at(word));
	}
	return tfidf;
}

// 计算余弦相似度
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

// 计算相似度并输出结果
void PlagiarismChecker::calculateAndSaveSimilarity(const string& outputFile) {
	// 分词
	vector<string> origTokens = tokenize(origText);
	vector<string> plagTokens = tokenize(plagText);

	// 计算TF
	map<string, int> origTF = computeTF(origTokens);
	map<string, int> plagTF = computeTF(plagTokens);

	// 计算IDF
	vector<vector<string>> documents = { origTokens, plagTokens };
	map<string, double> idf = computeIDF(documents);

	// 计算TF-IDF向量
	vector<double> origTFIDF = computeTFIDF(origTokens, origTF, idf);
	vector<double> plagTFIDF = computeTFIDF(plagTokens, plagTF, idf);

	// 计算余弦相似度
	double similarity = cosineSimilarity(origTFIDF, plagTFIDF);

	// 输出结果到文件
	ofstream outFile(outputFile);
	outFile << fixed << setprecision(2) << similarity << endl;
}

// 主函数：处理命令行参数并调用查重功能
int main(int argc, char* argv[]) {
	// 检查命令行参数数量
	if (argc != 4) {
		cerr << "Usage: " << argv[0] << " <original_file> <plagiarized_file> <output_file>" << endl;
		return 1;
	}

	try {
		// 创建查重对象
		PlagiarismChecker checker(argv[1], argv[2]);

		// 计算相似度并保存结果
		checker.calculateAndSaveSimilarity(argv[3]);

		cout << "Similarity calculation completed successfully." << endl;
	}
	catch (const exception& e) {
		cerr << "Error: " << e.what() << endl;
		return 1;
	}

	return 0;
}